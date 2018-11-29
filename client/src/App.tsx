import React, { Component } from "react"
import { BrowserRouter as Router, Route } from "react-router-dom"
import "./App.css"

import ProjectDashboard from "./views/projects"

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
          <Route exact path="/" component={ProjectDashboard} />
        </div>
      </Router>
    )
  }
}

export default App
