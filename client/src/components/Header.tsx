import React from "react"
import { Flex, Text, Box, Image, Button } from "rebass"
import { Route } from "react-router-dom"

import Container from "./Container"
import NavigationBar from "./NavigationBar"

import Logo from "./Logo.svg"

export default function() {
  return (
    <Flex
      px={2}
      pb={3}
      bg="white"
      color="white"
      css={{
        borderBottom: "1px solid #E5E5E5",
      }}
    >
      <Container flexWrap="wrap" width={1}>
        <Flex alignItems="center" width={1} mt={3}>
          <Image height={32} src={Logo} />
          <Box mx="auto" />
          <Button bg="#5B5F60" css={{ borderRadius: "10px" }}>
            Nouveau Projet
          </Button>
        </Flex>
        <Route path="/:projectId" component={NavigationBar} />
      </Container>
    </Flex>
  )
}
