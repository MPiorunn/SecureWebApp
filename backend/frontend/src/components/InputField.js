import React from 'react';
import axios from 'axios';


class InputField extends React.Component {


    componentDidMount() {
        // Simple POST request with a JSON body using axios
        // const article = { title: 'React POST Request Example' };
        // http://localhost:4000/http://localhost:8080
        axios.defaults.headers.get['Access-Control-Allow-Origin'] = 'http://localhost:3000' // for all requests
        axios.get(`http://localhost:8080`)
            .then(res => {
                console.log(res);
            })
            .catch(err => {
                console.log(err);
            })
    }

    render() {
        return (
            <div className="inputField">
                <input className="input"
                       type={this.props.type}
                       placeholder={this.props.placeholder}
                       value={this.props.value}
                       onChange={(e) => this.props.onChange(e.target.value)}
                />

            </div>
        );
    }
}

export default InputField;
