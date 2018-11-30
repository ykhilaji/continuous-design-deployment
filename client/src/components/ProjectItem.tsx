import React from "react"
import { Card, Heading } from "rebass"
import { withRouter, RouteComponentProps } from "react-router-dom"

import { Project } from "../model/project"

type Props = { project: Project }
type PropsWithRouter = RouteComponentProps<{}> & Props

const ProjectItem: React.SFC<PropsWithRouter> = ({
  history,
  project,
}: PropsWithRouter): JSX.Element => (
  <Card
    width={1}
    mb={3}
    p={3}
    borderRadius={8}
    border={1}
    borderColor="#E5E5E5"
    bg="white"
    href={`/${project.id}`}
    onClick={() => history.push(`/${project.id}`)}
    css={{
      "&:hover": {
        cursor: "pointer",
        boxShadow: "0 1px 20px rgba(0, 0, 0, 0.08)",
      },
    }}
  >
    <Heading>{project.name}</Heading>
    <p>{project.description}</p>
  </Card>
)

export default withRouter(ProjectItem)
