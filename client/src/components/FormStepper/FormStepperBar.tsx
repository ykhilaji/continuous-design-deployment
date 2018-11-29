import React from "react"
import { Flex } from "rebass"

import Container from "../Container"
import StepperItem from "./FormStepperItem"

export default function() {
  return (
    <Flex css={{ borderBottom: "1px solid #E5E5E5" }}>
      <Container width={1} my={3} px={2}>
        <StepperItem num={1} active={true} actionWord="configurer" subText="avec github" />
        <StepperItem num={2} actionWord="inviter" subText="des membres" showSeparator={false} />
      </Container>
    </Flex>
  )
}
