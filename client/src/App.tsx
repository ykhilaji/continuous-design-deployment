import { GoogleMap, Marker } from 'react-google-maps'
import withGoogleMap from 'react-google-maps/lib/withGoogleMap'
import withScriptjs from 'react-google-maps/lib/withScriptjs'

import * as React from 'react';
import { Link } from 'react-router-dom'
import { Redirect, Route, Switch } from 'react-router'

import './App.css'

import { HomePage } from 'src/HomePage'

export class App extends React.Component {
  public render() {
    return (
      <div className="app-container">
        <Header/>
        <Switch>
          <Route exact={ true } path="/home" render={ () => <HomePage/> } />
        </Switch>
      </div>
    );
  }
}

const Header = () => {
  return (
    <div className="header">
      <Link to="/home" > <img src="./assets/logo.png" alt="logo-site"/> </Link>
      <Link to="/" > CDD </Link>
    </div>
  )
}
