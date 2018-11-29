import React from "react"

import { Flex, FlexProps } from "rebass"

export default function(props: FlexProps & { children?: JSX.Element[] }) {
  return (
    <Flex
      {...props}
      width={1}
      flexDirection="column"
      css={{
        height: "100vh",
        maxHeight: "100vh",
        overflow: "hidden",
      }}
    />
  )
}
