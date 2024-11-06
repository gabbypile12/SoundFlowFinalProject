import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container, InputGroup, FormControl, Button, Row, Card } from 'react-bootstrap';
import Header from './Header.jsx';

const CLIENTID = "7015a03b8f2647269908ec04a5668d43";
const CLIENTSECRET = "b9c5e2d363634d88ae2ce82f643fe5e9";

export default function SearchComp() {
  const [searchInput, setSearchInput] = useState("");
  const [accessToken, setAccessToken] = useState("");
  const [songList, setSongList] = useState([]);
  const [btn, setBtn] = useState(Array(20).fill("Add Song"));
  const [btnColor, setBtnColor] = useState(Array(songList.length).fill("primary"));
  const { playlistId } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    let auth = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: 'grant_type=client_credentials&client_id=' + CLIENTID + '&client_secret=' + CLIENTSECRET
    };
    fetch('https://accounts.spotify.com/api/token', auth)
      .then(result => result.json())
      .then(data => setAccessToken(data.access_token))
      .catch(error => console.log('Spotify Authentication error'));
  }, []);

  async function search() {
      try{
        setBtn(Array(20).fill("Add Song"));
            setBtnColor(Array(songList.length).fill("primary"));
            let songParams = {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken
              }
            }
            await fetch('https://api.spotify.com/v1/search?q=' + searchInput + '&type=track', songParams)
              .then(response => response.json())
              .then(data => setSongList(data.tracks.items));
      } catch(error){
          console.log("Couldn't search for this song in the spotfiy api", error);
      }
  }

  async function addSong(song, index) {
    try{
        let myArtists = getArtist(song);
            await fetch(`http://localhost:8080/playlists/${playlistId}/songs`, {
              method: 'POST',
              headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
              },
              body: JSON.stringify({
                "songName": `${song.name}`,
                "previewUrl": `${song.album.images[0].url}`,
                "spotifyId": `${song.id}`,
                "album": "",
                "artists": [
                  {
                    "name": `${song.artists[0].name}`,
                    "spotifyId": `${song.artists[0].id}`,
                    "imageUrl": "",
                    "genre": ""
                  }
                ]
              }),
            }).then(console.log("done"));
            const newButtonText = [...btn];
            newButtonText[index] = "Added";
            setBtn(newButtonText);
            const newButtonColor = [...btnColor];
            newButtonColor[index] = "success";
            setBtnColor(newButtonColor);
    } catch(error){
        console.log("Couldn't add this song to the spotify playlist", error);
    }
  }

  function getArtist(song) {
    return JSON.stringify(song.artists.map(artist => ({
      "name": artist.name,
      "spotifyId": song.id,
      "imageUrl": "null",
      "genre": "null"
    })));
  }

  return (
    <div className="App">
      <Header />
      <h1 id="centered">Song Search</h1>
      <Container>
        <InputGroup className="mb-3" side="large">
          <FormControl
            placeholder='Search for song'
            type='input'
            onKeyPress={event => {
              if (event.key === "Enter") {
                search();
              }
            }}
            onChange={event => setSearchInput(event.target.value)}
          />
          <Button onClick={search}>Search</Button>
        </InputGroup>
      </Container>

      <Container className="mb-3">
        <Button variant="secondary" onClick={() => navigate(`/playlists/${playlistId}`)}>Back to Playlist</Button>
      </Container>

      <Container style={{padding:0}}>
        <Row style={{marginLeft: 8}} className='row-cols-6'>
          {songList.map((song, i) => (
            <Card style={{padding: 10, boxSizing: 'border-box', margin:5}}  bg="light" key={song.id}>
              <Card.Img  src={song.album.images[0].url} />
              <Card.Body style={{padding: 0}}>
                <Card.Title >{song.name}</Card.Title>
                <Card.Body style={{fontSize: 15, padding: 0}}>{song.artists[0].name}</Card.Body>
              </Card.Body>
              <Button variant={btnColor[i]} onClick={() => addSong(song, i)}>{btn[i]}</Button>
            </Card>
          ))}
        </Row>
      </Container>
    </div>
  );
}
