<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>
            <c:if test="${watch['new']}">New </c:if> Pet
        </h2>
        <form:form modelAttribute="watch"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${pet.id}"/>
            <div class="form-group has-feedback">                                
                <petclinic:inputField label="Date" name="date"/>
                <petclinic:inputField label="Start time:" name="beginTime"/>
                <petclinic:inputField label="End time:" name="finishTime"/>
                <div class="control-group">
                    <form:select path="type" label="Type " items="${types}" itemLabel="name" itemValue="name" size="5"/>
                </div>
                <div class="control-group">
                    <form:select path="vet" label="Vet " items="${vets}" itemLabel="fullname" itemValue="id" size="5"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${vet['new']}">
                            <button class="btn btn-default" type="submit">Add Watch</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Update Watch</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!watch['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>
