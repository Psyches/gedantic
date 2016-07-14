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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gedantic.analyzer.AAnalyzer;
import org.gedantic.analyzer.AResult;
import org.gedantic.analyzer.IndividualRelatedResult;
import org.gedantic.analyzer.comparator.IndividualResultSortComparator;
import org.gedantic.web.Constants;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.PersonalName;

/**
 * @author frizbog
 */
public class PeopleWithoutSurnamesAnalyzer extends AAnalyzer {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AResult> analyze(Gedcom g) {

        List<AResult> result = new ArrayList<>();

        for (Individual i : g.getIndividuals().values()) {
            if (i.getNames() == null || i.getNames().isEmpty()) {
                continue;
            }
            Set<String> personSurnames = getSurnamesFromIndividual(i);
            if (personSurnames.isEmpty() || (personSurnames.size() == 1 && personSurnames.contains(""))) {
                // Found a problem
                IndividualRelatedResult r = new IndividualRelatedResult(i, null, null, "Individual has no surnames");
                result.add(r);
            }
        }

        Collections.sort(result, new IndividualResultSortComparator());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "People who have names but no surnames";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "People without surnames";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultsTileName() {
        return Constants.URL_ANALYSIS_INDIVIDUAL_RESULTS;
    }

    /**
     * Get all the surnames for an individual
     * 
     * @param i
     *            the individual
     * @return a Set of all the surnames (as Strings)
     */
    private Set<String> getSurnamesFromIndividual(Individual i) {
        TreeSet<String> result = new TreeSet<String>();
        Pattern pattern = Pattern.compile(".*\\/(.*)\\/.*");
        for (PersonalName pn : i.getNames()) {
            if (pn.getSurname() != null) {
                result.add(pn.getSurname().getValue());
            }
            if (pn.getBasic() != null) {
                Matcher matcher = pattern.matcher(pn.getBasic());
                while (matcher.find()) {
                    result.add(matcher.group(1));
                }
            }
        }
        return result;
    }

}