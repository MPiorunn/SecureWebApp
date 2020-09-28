import React from "react";
import {Link} from "react-router-dom";
import InputField from "./InputField";
import SubmitButton from "./SubmitButton";

class PasswordResetForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            password: '',
            confirmPassword: '',
        }
    }

    async sendForm() {
        let passwordsTheSame = this.state.password !== '' && this.state.password === this.state.confirmPassword;
        console.log(passwordsTheSame);
    }

    setInputValue(property, val) {
        val = val.trim();
        //remove it
        if (val.length > 12) {
            return;
        }
        this.setState({
            [property]: val
        })
    }

    render() {
        return (
            <div className="loginForm">
                Reset Password
                <InputField
                    type='password'
                    placeholder='Password'
                    value={this.state.password ? this.state.password : ''}
                    onChange={(val) => this.setInputValue('password', val)}
                />

                <InputField
                    type='password'
                    placeholder='Confirm Password'
                    value={this.state.confirmPassword ? this.state.confirmPassword : ''}
                    onChange={(val) => this.setInputValue('confirmPassword', val)}
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
