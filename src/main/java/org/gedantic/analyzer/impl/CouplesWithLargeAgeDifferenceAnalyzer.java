/*
 * Copyright (c) 2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gedantic.analyzer.impl;

import java.util.ArrayList;
import java.util.List;

import org.gedantic.analyzer.AAnalyzer;
import org.gedantic.analyzer.AnalysisResult;
import org.gedantic.analyzer.AnalysisTag;
import org.gedantic.analyzer.DateAndString;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;

/**
 * An analyzer that finds couples where the spouses have birthdates 15 years or more apart
 * 
 * @author frizbog
 */
public class CouplesWithLargeAgeDifferenceAnalyzer extends AAnalyzer {

    /**
     * Number of milliseconds in sixty years
     */
    private static final long MILLIS_IN_FIFTEEN_YEARS = (long) (15 * 365.25 * 24 * 60 * 60 * 1000);

    /**
     * Number of milliseconds in a year
     */
    private static final long MILLIS_IN_YEAR = (long) (365.25 * 24 * 60 * 60 * 1000);

    @Override
    public List<AnalysisResult> analyze(Gedcom g) {
        List<AnalysisResult> result = new ArrayList<>();

        for (Family f : g.getFamilies().values()) {
            if (f.getHusband() == null || f.getWife() == null) {
                continue;
            }

            Individual husband = f.getHusband().getIndividual();
            DateAndString husbandLatestBirthDate = getBirthDate(husband, ImpreciseDatePreference.FAVOR_LATEST);

            Individual wife = f.getWife().getIndividual();
            DateAndString wifeLatestBirthDate = getBirthDate(wife, ImpreciseDatePreference.FAVOR_LATEST);

            // Both spouses need a birth date to proceed
            if (husbandLatestBirthDate == null || husbandLatestBirthDate.getDate() == null || wifeLatestBirthDate == null
                    || wifeLatestBirthDate.getDate() == null) {
                continue;
            }

            long diff = Math.abs(husbandLatestBirthDate.getDate().getTime() - wifeLatestBirthDate.getDate().getTime());

            if (diff >= MILLIS_IN_FIFTEEN_YEARS) {
                int yearsApart = (int) (diff / MILLIS_IN_YEAR);
                result.add(new AnalysisResult("Family", getFamilyDescriptor(f), null, null, "Husband born " + husbandLatestBirthDate
                        .getDateString() + ", wife born " + wifeLatestBirthDate.getDateString() + ", about " + yearsApart
                        + " years apart."));
            }
        }

        return result;
    }

    @Override
    public String getDescription() {
        return "Married couples with birth dates at least fifteen years apart";
    }

    @Override
    public String getName() {
        return "Couples with large age difference";
    }

    @Override
    public AnalysisTag[] getTags() {
        return new AnalysisTag[] { AnalysisTag.PROBLEM, AnalysisTag.FAMILIES };
    }

}
