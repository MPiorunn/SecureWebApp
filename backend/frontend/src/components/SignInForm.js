import React from 'react';
import InputField from "./InputField";
import SubmitButton from "./SubmitButton";

const PASSWORD_LENGTH = 10;

class SignInForm extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            confirmPassword: '',
            email: '',
            formErrors: {
                username: '',
                password: '',
                confirmPassword: '',
                email: ''
            }
        }
    }


    async sendForm() {
        console.log('xx');


        if (this.formValid(this.state.formErrors)) {
            console.log("is ok")
        } else {
            console.log("invalid")
        }
    }

    formValid = formErrors => {
        let valid = true;

        Object.values(formErrors).forEach(value => {
            console.log(value)
            value.length > 0 && (valid = false)
        })
        return valid;
    }

    validateEmail = email => {
        let valid = email.match(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);

        if (valid == null) {
            return "Invalid email format"
        }

        return "";
    }

    validatePassword = password => {
        if (password.length < PASSWORD_LENGTH) {
            return "Password must be at least " + PASSWORD_LENGTH + " characters long";
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

    setInputValue(property, val) {
        val = val.trim();

        let formErrors = this.state.formErrors;

        console.log("Name : " + property)
        console.log("Value : " + val)
        switch (property) {
            case "username":
                formErrors.username =
                    val.length < 3 && val.length > 0
                        ? "Minimum 3 characters required"
                        : "";
                break;
            case "password":
                formErrors.password = this.validatePassword(val);
                break;
            case "confirmPassword":
                formErrors.confirmPassword =
                    this.state.password.length <= 0 && this.state.password !== val
                        ? "Passwords do not match"
                        : "";
                break;
            case "email":
                formErrors.email = this.validateEmail(val);
                break;

            default:
                break;
        }

        this.setState({
            [property]: val,
            formErrors: formErrors
        }, () => console.log(this.state));
    }

    render() {
        const {formErrors} = this.state;

        return (
            <div className="loginForm">
                Sign In
                <InputField
                    type='type'
                    placeholder='Username'
                    value={this.state.username ? this.state.username : ''}
                    onChange={(val) => this.setInputValue('username', val)}
                />

                {formErrors.username.length > 0 && (<span className={"errorMessage"}>{formErrors.username}</span>)}

                <InputField
                    type='type'
                    placeholder='Email'
                    value={this.state.email ? this.state.email : ''}
                    onChange={(val) => this.setInputValue('email', val)}
                />

                {formErrors.email.length > 0 && (<span className={"errorMessage"}>{formErrors.email}</span>)}

                <InputField
                    type='password'
                    placeholder='Password'
                    value={this.state.password ? this.state.password : ''}
                    onChange={(val) => this.setInputValue('password', val)}
                />

                {formErrors.password.length > 0 && (<span className={"errorMessage"}>{formErrors.password}</span>)}


                <InputField
                    type='password'
                    placeholder='Confirm Password'
                    value={this.state.confirmPassword ? this.state.confirmPassword : ''}
                    onChange={(val) => this.setInputValue('confirmPassword', val)}
                />

                {formErrors.confirmPassword.length > 0 && (
                    <span className={"errorMessage"}>{formErrors.confirmPassword}</span>)}

                <SubmitButton
                    text='Sign In'
                    disabled={this.state.buttonDisabled}
                    onClick={() => this.sendForm()}
                />
            </div>
        );
    }
}

export default SignInForm;
