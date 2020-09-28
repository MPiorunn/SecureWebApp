import React from 'react';
import InputField from "./InputField";
import SubmitButton from "./SubmitButton";
import {Link} from "react-router-dom";
import axios from 'axios';
import {stringify} from "querystring";

class LoginForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
        }
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

    doLogin() {

        if (!this.state.username || !this.state.password) {
            return
        }

        const user = {
            'username': this.state.username,
            'password': this.state.password
        }

        console.log(user)
        axios.post(`http://localhost:8080/login`, stringify(user), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(res => {
            console.log(res);
        }).catch(err => {
            console.log(err);
        })
    }

    resetForm() {
        this.setState({
            username: '',
            password: '',
        })
    }

    render() {
        return (
            <div className="loginForm">
                Log in
                <InputField
                    type='text'
                    placeholder='Username'
                    value={this.state.username ? this.state.username : ''}
                    onChange={(val) => this.setInputValue('username', val)}
                />
                <InputField
                    type='password'
                    placeholder='Password'
                    value={this.state.password ? this.state.password : ''}
                    onChange={(val) => this.setInputValue('password', val)}
                />

                <SubmitButton
                    text='Login'
                    onClick={() => this.doLogin()}
                />

                <Link to="/signIn" className="redir">Create an account.</Link>
                <Link to="/reset" className="redir">Forgot password?</Link>
            </div>

        );
    }
}

export default LoginForm;
