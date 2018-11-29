import React from "react"

import { Flex, FlexProps } from "rebass"

export default function(props: FlexProps & { children : JSX.Element[] }) {
  return <Flex {...props} mx='auto' css={{ maxWidth: "1024px" }} />
}
