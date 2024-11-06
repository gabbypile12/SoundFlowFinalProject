import graphic from './iphone_graphic.png';
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './Header.jsx';
import Button from 'react-bootstrap/Button';
import './App.css';
import {Link } from "react-router-dom";
import Playlists from './Playlists';

function LandingPage(){
    return(
        <div>
            <Header></Header>
            <div className = "containerLanding">
                <div className = "child" id = "textHolder">
                    <h2> Welcome to </h2>
                    <h1> SoundFlow </h1>
                    <h3> Create your own personalized playlist using spotify api </h3>
                    <Link to="/playlists"> <Button variant="outline-light">Create Playlists</Button>{' '}</Link>
                </div>
                <div className = "child">
                    <img className ="landingImage" src = {graphic} />
                </div>
            </div>

        </div>


    );
}
export default LandingPage;