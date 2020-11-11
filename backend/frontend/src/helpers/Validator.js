class Validator {

    static INVALID_USERNAME = 'Minimum 3 characters required'
    static INVALID_PASSWORD = 'Password empty'
    static INVALID_CONFIRMATION = "Passwords do not match"
    static INVALID_EMAIL = "Invalid email format"
    static PASSWORD_LENGTH = 10;


    static validateUsername = username => {
        return username.length < 3
            ? Validator.INVALID_USERNAME
            : "";
    }

    static validateConfirmedPassword = (password, confirmed) => {
        return confirmed <= 0 || password !== confirmed
            ? Validator.INVALID_CONFIRMATION
            : "";
    }

    static validateEmail = email => {
        if (email === "") {
            return "";
        }

        let valid = email.match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);

        if (valid == null) {
            return Validator.INVALID_EMAIL
        }

        return "";
    }

    static validatePassword = password => {
        if (password === "") {
            return "";
        }

        if (password.length < Validator.PASSWORD_LENGTH) {
            return "Password must be at least " + Validator.PASSWORD_LENGTH + " characters long";
        }

        if (password.toLowerCase() === password || password.toUpperCase() === password) {
            return "Password must contain at least one capital and lower character";
        }

        let hasNumber = password.match(/\d+/g)

        if (hasNumber === null) {
            return "Password must contain at least one number";
        }

        let hasSpecialChar = password.match("(?=.*[!@#$%^&*])")

        if (hasSpecialChar === null) {
            return "Password must contain at least special character";
        }

        return "";
    }

    static validateForm = formErrors => {
        let valid = true;

        Object.values(formErrors).forEach(value => {
            value.length > 0 && (valid = false)
        })
        return valid;
    }
}

export default Validator;