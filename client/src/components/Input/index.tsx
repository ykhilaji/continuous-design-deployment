import React from "react"
import { Flex, Text } from "rebass"
import InputStyles from "./input.module.css"

interface Props {
  placeholder?: string
  prefix?: string
  value?: string
}

const Input = ({ value, placeholder, prefix }: Props) => {
  return (
    <Flex width={1} css={{ position: "relative" }}>
      {prefix && (
        <Text
          color="#aaa"
          css={{ position: "absolute", top: "16px", left: "15px" }}
        >
          {prefix}
        </Text>
      )}
      <input
        className={InputStyles.root}
        placeholder={placeholder}
        value={value}
        style={{
          paddingLeft: !!prefix ? "70px" : "20px",
        }}
      />
    </Flex>
  )
}

export default Input
