import React from "react"
import { Flex, Text, Box } from "rebass" 

export default function() {
  return <Flex
      px={2}
      py={3}
      bg='black'
      color='white'
      alignItems="center">
      <Text p={2} fontWeight='bold'>Pipeline</Text>
      <Box mx='auto' />
    </Flex>
}
