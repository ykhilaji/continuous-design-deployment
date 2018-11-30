import React from "react"
import { Flex } from "rebass"

import { Project } from '../../model/project'
import ProjectsList from '../../components/ProjectList'
import { listProjects } from '../../services/projects'

interface State {
  projects?: Project[]
  projectsFetchingError?: string
}

class ProjectDashboard extends React.Component<{}, State> {

  state: State = {}

  public render(): JSX.Element {
    return <Flex>
      { this.state.projects 
          ? this.renderProjects(this.state.projects)
          : this.renderErrorFetchingProjects()
      }
    </Flex>
  }

  componentDidMount() {
    listProjects()
      .then((projects) => this.setState({ projects }))
      .catch((error) => this.setState({ projectsFetchingError: error.message }))
  }

  private renderProjects(projects: Project[]): JSX.Element {
    return <ProjectsList projects={projects} />
  }

  private renderErrorFetchingProjects(): JSX.Element {
    return <p>{ this.state.projectsFetchingError || 'Unknown error while fetching projects' }</p>
  }

}

export default ProjectDashboard
