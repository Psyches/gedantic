<%--

    Copyright (c) 2016 Matthew R. Harrah

    MIT License

    Permission is hereby granted, free of charge, to any person
    obtaining a copy of this software and associated documentation
    files (the "Software"), to deal in the Software without
    restriction, including without limitation the rights to use,
    copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the
    Software is furnished to do so, subject to the following
    conditions:

    The above copyright notice and this permission notice shall be
    included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
    OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
    NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
    HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
    WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
    OTHER DEALINGS IN THE SOFTWARE.

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://classtagfunctions" prefix="class"%>


<dl class="dl-horizontal">
	<c:if test="${not empty result.factType}">
		<dt>Fact type</dt>
		<dd>${fn:escapeXml(result.factType)}</dd>
	</c:if>
	<c:if test="${not empty result.value}">
		<dt>Value</dt>
		<c:choose>
			<c:when test="${class:instanceOf(result.value, 'org.gedcom4j.model.Individual') }">
				<dd>
				${result.value.formattedName} 
				<span class="glyphicon glyphicon-info-sign" data-toggle="tooltip" title="${fn:escapeXml(result.value)}"></span>
				</dd>
			</c:when>
			<c:otherwise>
				<dd>${fn:escapeXml(result.value)}</dd>
			</c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${not empty result.problem}">
		<dt>Problem</dt>
		<dd>${fn:escapeXml(result.problem)}</dd>
	</c:if>
</dl>