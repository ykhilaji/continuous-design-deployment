import React from "react"
import { Flex, Box } from "rebass" 
// import Container from "../../components/Container"
import FileTree from "./FileTree"

const PipelineIndex = () => {
  return <Flex flexDirection="column" css={{ height: "100%" }}>
      <Box bg='white'  css={{ height: "100%", maxWidth: "340px", borderRight: "1px solid #E5E5E5" }}>
        <FileTree />
      </Box>  
    </Flex>
}

export default PipelineIndex
