import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import SearchComp from './SearchComp';
import PlaylistsEditor from './PlaylistsEditor';
import Playlists from './Playlists';
import Header from './Header';
import LandingPage from './LandingPage';


function App() {
  return (
    <Router>
      <Routes>
          <Route path="/" element={<LandingPage />} />
        <Route path="/playlists" element={<Playlists />} />
        <Route path="/playlists/:playlistId" element={<PlaylistsEditor />} />
        <Route path="/search/:playlistId" element={<SearchComp />} />
      </Routes>
    </Router>
  );
}

export default App;
