import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container, Button, Row, Card, Alert, Modal, Form} from 'react-bootstrap';
import Header from './Header.jsx';

export default function PlaylistsEditor() {
  const { playlistId } = useParams();
  const [songs, setSongs] = useState([]);
  const [playlistName, setPlaylistName] = useState("");
  const [updatePlaylistName, setUpdatePlaylistName] = useState("");
  const [loading, setLoading] = useState(true);
  const [show, setShow] = useState(false);
  const navigate = useNavigate();

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  

  useEffect(() => {
    fetchSongs();
  }, [playlistId]);

  const fetchSongs = async () => {
    try {
      const response = await fetch(`http://localhost:8080/playlists/${playlistId}`);
      const data = await response.json();
      setPlaylistName(data.playlistName);
      setSongs(data.songs);
    } catch (error) {
      console.error("Error fetching songs:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddSongs = () => {
    navigate(`/search/${playlistId}`);
  };

  const confirmDelete = (songId) => {
    if (window.confirm("Are you sure you want to remove this song from the playlist?")) {
      deleteSong(songId);
    }
  };

  async function handleSave(){
    await fetch(`http://localhost:8080/playlists/${playlistId}/`, {
      method: 'PUT',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: 
        JSON.stringify({id:playlistId,
          playlistName:updatePlaylistName
        })
    });
    fetchSongs();
    setShow(false);
  
  }

  async function deleteSong(songId) {
    await fetch(`http://localhost:8080/playlists/${playlistId}/songs/${songId}`, {
      method: 'DELETE',
    });

    setSongs(songs.filter((s) => s.id !== songId));
  }

  return (
    <Container className="my-4">
      <Header />
      <h1 id = "centered"> {playlistName}</h1>
      <Button className="mb-3" onClick={handleShow}>Update Playlist Name</Button>
      <div>
        <Button className="mb-3" onClick={handleAddSongs}>Add Songs</Button>
      </div>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Update</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
              <Form.Label>Enter New Playlist Name</Form.Label>
              <Form.Control
                type="text"
                placeholder="My new playlist"
                onChange={(event) => setUpdatePlaylistName(event.target.value)}
                autoFocus
              />
            </Form.Group>
            </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" onClick={handleSave}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>
      
      
      {loading ? (
        <Alert variant="info">Loading songs...</Alert>
      ) : songs.length === 0 ? (
           <Alert variant="warning">No songs in this playlist. Click "Add Songs" to get started!</Alert>
      ) : (
        <Container style={{padding: 0}}>
          <Row className="mx-2 row row-cols-5">
            {songs.map((song) => (
              <Card style={{padding: 15, boxSizing: 'border-box', margin: 3}}  bg="light" key={song.id} className="mb-3">
                <Card.Img src={song.previewUrl} />
                <Card.Body>
                  <Card.Title style={{fontSize: 25}} className="cardTitle">{song.songName}</Card.Title>
                  <Card.Text style={{fontSize: 15}}>{song.artists[0].name}</Card.Text>
                  <Button variant="danger" onClick={() => confirmDelete(song.id)}>Remove</Button>
                </Card.Body>
              </Card>
            ))}
          </Row>
        </Container>
      )}
    </Container>
  );
}
