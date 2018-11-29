import React, { Component } from "react"
import { BrowserRouter as Router, Route } from "react-router-dom"

import ProjectDashboard from "./views/projects"
import Project from "./views/project"

import Header from "./components/Header"
import AppLayout from "./components/AppLayout"

class App extends Component {
  render() {
    return (
      <Router>
        <AppLayout bg="#F7F7F7">
          <Header />
          <Route exact path="/" component={ProjectDashboard} />
          <Route path="/:projectId" component={Project} />
        </AppLayout>
      </Router>
    )
  }
}

export default App
