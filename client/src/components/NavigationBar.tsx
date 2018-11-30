import React from "react"
import { Flex, Text, Image, Box } from "rebass"
import { Link } from "react-router-dom"
import { RouteComponentProps } from "react-router-dom"
import ProjectName from "../services/ProjectName"

export default function(props: RouteComponentProps<{ projectId: string }>) {
  const projectId = props.match.params.projectId
  const projectName = ProjectName(projectId)

  return (
    <Flex color="black" alignItems="center" py={1} mt={4}>
      <Link to="/">
        <Image
          width={24}
          height={24}
          src="https://icon.now.sh/arrow_back"
          alt="arrow icon"
        />
      </Link>
      <Text p={2} fontWeight="bold" fontSize={28}>
        {projectName}
      </Text>
      <Box mx="auto" />
    </Flex>
  )
}
