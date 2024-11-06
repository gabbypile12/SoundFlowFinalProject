import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import logo from './logo.png';
import {Link } from "react-router-dom";

function Header(){
    return(
        <div>
             <Navbar bg="MyColor" data-bs-theme="dark">
                    <Container>
                      <Navbar.Brand href="#home"> <a class="navbar-brand" href="#"> <img src= {logo} atl = "logo"  width="300px" height="60px" /></a></Navbar.Brand>
                      <Navbar.Collapse className="justify-content-end">
                        <Nav.Link href="\" className="aNavLink">Home</Nav.Link> <br/>
                        <Nav.Link href="\playlists" className="aNavLink">Playlist</Nav.Link>
                      </Navbar.Collapse>
                    </Container>
             </Navbar>
        </div>
    );
}
export default Header;