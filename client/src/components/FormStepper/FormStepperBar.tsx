import React from "react"
import { Flex } from "rebass"

import Container from "../Container"
import StepperItem from "./FormStepperItem"

interface Props {
  activeIndex?: number
}

export default function({ activeIndex = -1 }: Props) {
  return (
    <Flex css={{ borderBottom: "1px solid #E5E5E5" }}>
      <Container width={1} my={3} px={2}>
        <StepperItem num={1} active={activeIndex === 1} actionWord="configurer" subText="avec github" />
        <StepperItem num={2} active={activeIndex === 2} actionWord="inviter" subText="des membres" showSeparator={false} />
      </Container>
    </Flex>
  )
}
