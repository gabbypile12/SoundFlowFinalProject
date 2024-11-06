import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { Container, InputGroup, FormControl, Button, Row, Card } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Header from './Header.jsx';
import mixtape from './mixtape.png';

export default function Playlists() {
  const [playlists, setPlaylists] = useState([]);
  const [playlistInput, setPlaylistInput] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchPlaylists();
  }, []);

  const fetchPlaylists = async () => {
      try{
          const response = await fetch('http://localhost:8080/playlists');
          const data = await response.json();
          setPlaylists(data);
      } catch(error){
          console.log("Couldn't connect to the database", error);
      }
  };

  async function addPlaylist() {
     try{
         await fetch('http://localhost:8080/playlists', {
               method: 'POST',
               headers: {
                 'Accept': 'application/json',
                 'Content-Type': 'application/json',
               },
               body: JSON.stringify({ playlistName: playlistInput }),
             });
             setPlaylistInput('');
             fetchPlaylists();
     } catch(error){
         alert("Cound add this playlist");
     }

  }

  async function deletePlaylist(playlistId) {
      try{
          await fetch(`http://localhost:8080/playlists/${playlistId}`, {
                method: 'DELETE',
          });
          setPlaylists(playlists.filter((p) => p.id !== playlistId));
      } catch(error){
          console.log("Couldn't delete this song from the playlist", error);
      }

  }

  const goToEditPlaylist = (playlistId) => {
    navigate(`/playlists/${playlistId}`);
  };

  return (
    <div>
      <Header />
      <h1 id="centered">Playlists</h1>
      <Container>
        <InputGroup>
          <FormControl
            placeholder="Type in new playlist name"
            type="input"
            value={playlistInput}
            onChange={(event) => setPlaylistInput(event.target.value)}
          />
          <Button
            variant="danger"
            onClick={() => (playlistInput !== '' ? addPlaylist() : null)}
          >
            Add Playlist
          </Button>
        </InputGroup>
      </Container>
      <Container className="playlistContainer">
        <Row style={{margin: 4}} className="row-cols-2 g-4">
          {playlists.map((playlist) => (
            <Card className="cardPlaylist" key={playlist.id}>
              <Card.Body>
                <Card.Title>{playlist.playlistName}</Card.Title>
              </Card.Body>
              <img src={mixtape} alt="Mixtape" />
              <div>
                <Button
                  className="editPlaylistButton"
                  onClick={() => goToEditPlaylist(playlist.id)}
                >
                  Edit
                </Button>
                <Button
                  className="editPlaylistButton"
                  onClick={() => deletePlaylist(playlist.id)}
                  variant="danger">
                  Delete
                </Button>
              </div>
            </Card>
          ))}
        </Row>
      </Container>
    </div>
  );
}
