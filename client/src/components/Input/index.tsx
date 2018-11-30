import React, { ChangeEvent } from "react"
import { Flex, Text } from "rebass"
import InputStyles from "./input.module.css"

interface Props {
  placeholder?: string
  prefix?: string
  value?: string
  onChange?: (event: ChangeEvent<HTMLInputElement>) => void
}

const Input = ({ value, placeholder, prefix, onChange }: Props) => {
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
        onChange={onChange}
      />
    </Flex>
  )
}

export default Input
