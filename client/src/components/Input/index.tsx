import React from "react"
import { Flex } from "rebass"
import InputStyles from "./input.module.css"

interface Props {
  placeholder?: string
}

const Input = ({ placeholder }: Props) => {
  return (
    <Flex width={1}>
      <input className={InputStyles.root} placeholder={placeholder} />
    </Flex>
  )
}

export default Input
