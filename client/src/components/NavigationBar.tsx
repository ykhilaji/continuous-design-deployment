import React from "react"
import { Flex, Text, Image, Box } from "rebass"
import { Link } from "react-router-dom"

export default function() {
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
        Project Name
      </Text>
      <Box mx="auto" />
    </Flex>
  )
}
