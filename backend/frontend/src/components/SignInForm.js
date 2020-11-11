import React from 'react';
import InputField from "./InputField";
import SubmitButton from "./SubmitButton";
import {Link} from "react-router-dom";
import axios from 'axios';
import {stringify} from "querystring";
import Validator from "../helpers/Validator";


class SignInForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            confirmPassword: '',
            email: '',
            backendFeedback: '',
            formErrors: {
                username: Validator.INVALID_USERNAME,
                password: Validator.INVALID_PASSWORD,
                confirmPassword: Validator.INVALID_CONFIRMATION,
                email: Validator.INVALID_EMAIL
            }
        }
    }

    async sendForm() {
        if (Validator.validateForm(this.state.formErrors)) {
            let data = {
                username: this.state.username,
                password: this.state.password,
                email: this.state.email
            }
            axios.post(`http://localhost:8080/add`, stringify(data), {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(res => {
                if (res.status === 200) {
                    let resUser = res.data.username
                    let resEmail = res.data.email
                    if (resUser === this.state.username && resEmail === this.state.email) {
                        this.setState({
                            backendFeedback: "Login successful"
                        })
                    }
                }
            }).catch(err => {
                this.resetForm()
                this.setState({
                    backendFeedback: err.response.data.message
                })
            })
        } else {
            this.setState({
                backendFeedback: this.getValidationErrorMessage(this.state.formErrors)
            })
        }
    }

    getValidationErrorMessage = formErrors => {
        let message = ''
        Object.values(formErrors).forEach(value => {
            message += value
        })
        return message;
    }


    resetForm() {
        this.setState({
            username: '',
            password: '',
            confirmPassword: '',
            email: '',
            backendFeedback: '',
            formErrors: {
                username: '',
                password: '',
                confirmPassword: '',
                email: ''
            }
        })
    }

    setInputValue(property, val) {
        val = val.trim();

        let formErrors = this.state.formErrors;

        console.log("Name : " + property)
        console.log("Value : " + val)
        switch (property) {
            case "username":
                formErrors.username = Validator.validateUsername(val)
                break;
            case "password":
                formErrors.password = Validator.validatePassword(val);
                break;
            case "confirmPassword":
                formErrors.confirmPassword = Validator.validateConfirmedPassword(this.state.password, val)
                break;
            case "email":
                formErrors.email = Validator.validateEmail(val);
                break;
            default:
                break;
        }

        this.setState({
            [property]: val,
            formErrors: formErrors
        }, () => console.log(this.state.formErrors));
    }

    render() {
        return (
            <div className="loginForm">
                <div className="title">Create an account</div>
                <InputField
                    type='type'
                    placeholder='Username'
                    value={this.state.username ? this.state.username : ''}
                    onChange={(val) => this.setInputValue('username', val)}
                />

                {/*{formErrors.username.length > 0 && (<span className={"errorMessage"}>{formErrors.username}</span>)}*/}

                <InputField
                    type='type'
                    placeholder='Email'
                    value={this.state.email ? this.state.email : ''}
                    onChange={(val) => this.setInputValue('email', val)}
                />

                {/*{formErrors.email.length > 1 && (<span className={"errorMessage"}>{formErrors.email}</span>)}*/}

                <InputField
                    type='password'
                    placeholder='Password'
                    value={this.state.password ? this.state.password : ''}
                    onChange={(val) => this.setInputValue('password', val)}
                />

                {/*{formErrors.password.length > 0 && (<span className={"errorMessage"}>{formErrors.password}</span>)}*/}


                <InputField
                    type='password'
                    placeholder='Confirm Password'
                    value={this.state.confirmPassword ? this.state.confirmPassword : ''}
                    onChange={(val) => this.setInputValue('confirmPassword', val)}
                />

                {/*{formErrors.confirmPassword.length > 0 && (*/}
                {/*    <span className={"errorMessage"}>{formErrors.confirmPassword}</span>)}*/}


                {this.state.backendFeedback}
                <SubmitButton
                    text='Sign In'
                    onClick={() => this.sendForm()}
                />

                <Link to="/login" className="redir">Already have an account? Login</Link>
            </div>
        );
    }
}

export default SignInForm;
