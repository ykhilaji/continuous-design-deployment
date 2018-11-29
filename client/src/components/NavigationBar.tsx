import React from "react"
import { Flex, Text, Image } from "rebass"
import Container from "./Component"

export default function() {
  return (
    <Flex bg="lightgrey" color="black" alignItems="center" py={1} px={2}>
      <Container>
        <Image
          width={20}
          src="https://icon.now.sh/keyboard_backspace"
          alt="arrow icon"
        />
        <Text p={2}>Project Name</Text>
      </Container>
    </Flex>
  )
}
