import React from "react"
import { Flex, Text, Box, Image, Button } from "rebass"
import Container from "./Component"

import Logo from "./Logo.svg"

export default function() {
  return (
    <Flex
      px={2}
      py={3}
      bg="white"
      color="white"
      css={{
        borderBottom: "1px solid #E5E5E5",
      }}
    >
      <Container width={1}>
        <Image height={32} src={Logo} />
        <Box mx="auto" />
        <Button bg="#5B5F60" css={{ borderRadius: "10px" }}>
          Nouveau Projet
        </Button>
      </Container>
    </Flex>
  )
}
