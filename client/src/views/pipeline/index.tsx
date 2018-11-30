import React, { useState } from "react"
import { Flex, Box } from "rebass" 

import FileTree from "./FileTree"
import EmptyPipeline from "./PipelineEmpty"
import PipelineParameters from "./PipelineParameters"
import { FigmaNode } from "../../model/figmaNode"

const PipelineIndex = () => {
  const [node, setActiveNode] = useState<FigmaNode>({id: "", name: "", type: ""})
  return <Flex width={1} css={{ height: "100%" }}>
      <Box bg='white'  css={{ height: "100%", width: "340px", borderRight: "1px solid #E5E5E5" }}>
        <FileTree onClick={setActiveNode}/>
      </Box>
      {
        true
        ? <PipelineParameters assetName={node.name} />
        : <EmptyPipeline />
      }
    </Flex>
}

export default PipelineIndex
