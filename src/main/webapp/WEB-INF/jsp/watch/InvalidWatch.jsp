<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="Provided Cares">
    <jsp:attribute name="customScript">
    </jsp:attribute>
    <jsp:body>
        <h2>
        	INVALID WATCH!
    	</h2>
        <p>The watch is not valid, there are concurrent watches for the same vet.</p>    
    </jsp:body>
</petclinic:layout>
