import React from 'react';
import './App.css';
import {observer} from 'mobx-react'
import LoginForm from "./components/LoginForm";
import SignInForm from "./components/SignInForm";
import PasswordResetForm from "./components/PasswordResetForm";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

class App extends React.Component {

    // async componentDidMount() {
    //     try {
    //
    //         let res = await fetch('/isLoggedIn', {
    //             method: 'post',
    //             headers: {
    //                 'Accept': "application/json",
    //                 'Content-Type': 'application/json'
    //             }
    //         })
    //
    //
    //         let result = await res.json();
    //         if (result && result.success) {
    //             UserStore.isLoggedIn = true;
    //             UserStore.loading = false;
    //             UserStore.username = result.username
    //         } else {
    //             UserStore.loading = false;
    //             UserStore.isLoggedIn = false;
    //         }
    //
    //     } catch (e) {
    //         UserStore.loading = false;
    //         UserStore.isLoggedIn = false;
    //     }
    // }
    //
    // async doLogout() {
    //     try {
    //
    //         let res = await fetch('/logout', {
    //             method: 'post',
    //             headers: {
    //                 'Accept': "application/json",
    //                 'Content-Type': 'application/json'
    //             }
    //         })
    //
    //         let result = await res.json();
    //         if (result && result.success) {
    //             UserStore.isLoggedIn = false;
    //             UserStore.username = '';
    //         }
    //     } catch (e) {
    //         console.log(e);
    //     }
    // }

    render() {
        // if (UserStore.loading) {
        //     return (
        //         <div className="app">
        //             <div className="container">
        //                 Loading, please wait...
        //             </div>
        //         </div>
        //     );
        // }
        // else
        //     {

        // if (UserStore.isLoggedIn) {
        //     return (
        //         <div className="app">
        //             <div className="container">
        //                 Welcome {UserStore.username}
        //
        //                 <div className="wrapper">
        //                     <SubmitButton
        //                         text={"Log out"}
        //                         disabled={false}
        //                         onClick={() => this.doLogout()}
        //                     />
        //                 </div>
        //
        //             </div>
        //         </div>
        //     );
        // }


        return (
            <Router>

                <div className="app">
                    <div className="container">
                        <div className="wrapper">
                            <Switch>
                                <Route path="/reset" component={PasswordResetForm}/>
                                <Route component={LoginForm}/>
                                {/*<Route path="/login" component={LoginForm}/>*/}
                                <Route path="/signIn" component={SignInForm}/>
                            </Switch>
                        </div>
                    </div>
                </div>


            </Router>

        );
    }

    // }
}

export default observer(App);
