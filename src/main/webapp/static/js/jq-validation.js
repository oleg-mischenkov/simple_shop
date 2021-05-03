$(document).ready(function(){

	const NAME_REG = /^([^\d|^\s|\W]{3,})$/;
	const EMAIL_REG =/(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;
	const PSW_REG = /^([\w\W]{6,})$/;

	inputValidation(PSW_REG, "#form-login", ".f-psw", 2);
	inputValidation(EMAIL_REG, "#form-login", ".f-email", 2);

	inputValidation(NAME_REG, "#form-sing-up", ".f-name", 5);
	inputValidation(NAME_REG, "#form-sing-up", ".f-second-name", 5);
	inputValidation(EMAIL_REG, "#form-sing-up", ".f-email", 5);
	inputValidation(PSW_REG, "#form-sing-up", ".f-psw", 5);
	inputValidation(PSW_REG, "#form-sing-up", ".f-psw-2", 5);

	function inputValidation(reg, formName, currentClass, inputCount) {
		$(formName + " " + currentClass).keyup(function(){
			let tValue = $(this).val();

			if(reg.test(tValue)) {
				$(this).removeClass("f-invalid");
				$(this).addClass("is-valid");

				let validInput = $(formName + " input.is-valid").length;

				if(validInput == inputCount) {
					$(formName + " button").prop("disabled", false);
				}

			} else {
				$(this).addClass("f-invalid");
			}
		});
	};
});