import React from "react"
import { Flex, Box } from "rebass" 

import FileTree from "./FileTree"
import EmptyPipeline from "./PipelineEmpty"
import PipelineParameters from "./PipelineParameters"

const PipelineIndex = () => {
  return <Flex width={1} css={{ height: "100%" }}>
      <Box bg='white'  css={{ height: "100%", width: "340px", borderRight: "1px solid #E5E5E5" }}>
        <FileTree />
      </Box>
      {
        true
        ? <PipelineParameters assetName="Group" />
        : <EmptyPipeline />
      }
    </Flex>
}

export default PipelineIndex
