import React from 'react';
import InputField from "./InputField";
import SubmitButton from "./SubmitButton";

class SignInForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            confirmPassword: '',
            email: ''
        }
    }

    async sendForm() {
        console.log('xx');
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
                    type='type'
                    placeholder='Username'
                    value={this.state.username ? this.state.username : ''}
                    onChange={(val) => this.setInputValue('username', val)}
                />

                <InputField
                    type='type'
                    placeholder='Email'
                    value={this.state.email ? this.state.email : ''}
                    onChange={(val) => this.setInputValue('email', val)}
                />

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
                    text='Sign In'
                    disabled={this.state.buttonDisabled}
                    onClick={() => this.sendForm()}
                />
            </div>
        );
    }
}

export default SignInForm;
