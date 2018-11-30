import React from "react"
import { Flex, Heading } from "rebass"

import { Project } from "../model/project"

import Container from "./Container"
import ProjectItem from "./ProjectItem"

type Props = { projects: Project[] }

export default function(props: Props): JSX.Element {
  return (
    <Flex alignItems="center" justifyContent="center" width={1}>
      <Container flexDirection="column" width={1}>
        <Heading mt={4} mb={3}>Mes projets</Heading>
        {props.projects.map(project => <ProjectItem key={project.id} project={project} />)}
      </Container>
    </Flex>
  )
}
