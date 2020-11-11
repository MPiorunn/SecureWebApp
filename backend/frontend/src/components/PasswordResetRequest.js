import React from "react";
import {Link} from "react-router-dom";
import InputField from "./InputField";
import SubmitButton from "./SubmitButton";
import axios from "axios";
import {stringify} from "querystring";
import Validator from "../helpers/Validator";

class PasswordResetForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '',
            formErrors: {
                email: Validator.INVALID_EMAIL
            }
        }
    }

    resetForm() {
        this.setState({
            email: '',
            formErrors: {
                email: ''
            }
        })
    }

    async sendForm() {

        if (!Validator.validateForm(this.state.formErrors)) {
            return
        }
        const data = {
            'email': this.state.email,
        }
        axios.post(`http://localhost:8080/reset`, stringify(data), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(res => {
            console.log(res);
        }).catch(err => {
            console.log(err)
        })
        this.resetForm()
    }

    setInputValue(val) {
        val = val.trim();
        let formErrors = this.state.formErrors
        formErrors.error = Validator.validateEmail(val);
        this.setState({
            email: val,
            formErrors: formErrors
        })
    }

    render() {
        return (
            <div className="loginForm">
                Password reset request
                <InputField
                    type='text'
                    placeholder='Email'
                    value={this.state.email ? this.state.email : ''}
                    onChange={(val) => this.setInputValue(val)}
                />

                <SubmitButton
                    text='Reset password'
                    onClick={() => this.sendForm()}
                />

                <Link to="/login" className="redir">Login</Link>
            </div>
        );
    }
}

export default PasswordResetForm;
