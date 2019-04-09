<input id="password" name="password" type="password"/>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="pwstrength.js"></script>
<script type="text/javascript">
$(document).ready(function () {
    options = {
        common: {minChar:8},
        ui: {
            showVerdictsInsideProgressBar:true,
            showErrors:true,
            errorMessages:{
                wordLength: '<spring:message code="error.wordLength"/>',
                wordNotEmail: '<spring:message code="error.wordNotEmail"/>',
                wordSequences: '<spring:message code="error.wordSequences"/>',
                wordLowercase: '<spring:message code="error.wordLowercase"/>',
                wordUppercase: '<spring:message code="error.wordUppercase"/>',
                wordOneNumber: '<spring:message code="error.wordOneNumber"/>',
                wordOneSpecialChar: '<spring:message code="error.wordOneSpecialChar"/>'
            }
        }
    };
    $('#password').pwstrength(options);
});
</script>