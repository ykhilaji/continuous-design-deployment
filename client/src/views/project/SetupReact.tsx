import React from "react"
import { Box, Flex } from "rebass"

import Container from "../../components/Container"
import Heading from "../../components/Text/Heading"
import Input from "../../components/Input"

const SetupReact = () => {
  
  return <Container flexDirection="column" width={1}>
      <Box mt={5} />
      <Flex flexDirection="column" width={1} css={{ maxWidth: "700px"}}>
        <Heading mb={3}>Lier le projet à GitHub</Heading>
        <Input placeholder="Saisissez l’URL GitHub du projet à importer"/>
        <Box mt={4} />
        <Heading mb={3}>Identifier un dossier GitHub de destination</Heading>
        <Input placeholder="Saisissez l’adresse de GitHub de destination" />
      </Flex>
    </Container> 
}

export default SetupReact
