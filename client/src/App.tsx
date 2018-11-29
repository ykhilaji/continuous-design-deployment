import React, { Component } from "react"
import { BrowserRouter as Router, Route } from "react-router-dom"

import ProjectDashboard from "./views/projects"
import Project from "./views/project"

import Header from "./components/Header"

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
          <Header />
          <Route exact path={["", "/"]} component={ProjectDashboard} />
          <Route path="/:projectId" component={Project} />
        </div>
      </Router>
    )
  }
}

export default App
