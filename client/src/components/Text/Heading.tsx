import React from "react"
import { Heading, HeadingProps } from "rebass"

export default function(props: HeadingProps & { children: React.ReactNode}) {
  return <Heading {...props} fontSize={18} />
}
