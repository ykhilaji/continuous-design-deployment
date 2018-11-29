// Type definitions for Rebass 0.3.8
// Project: https://github.com/jxnblk/rebass
// Definitions by: rhysd <https://rhysd.github.io>
//                 ryee-dev <https://github.com/ryee-dev>
//                 jamesmckenzie <https://github.com/jamesmckenzie>
// Definitions: https://github.com/DefinitelyTyped/DefinitelyTyped
// TypeScript Version: 2.8


declare module "rebass" {

  import * as React from "react"

  export interface BaseProps<C> extends React.ClassAttributes<C> {
    className?: string
    as?: string
  }

  export interface SpaceProps<C> extends BaseProps<C> {
    m?: number
    mt?: number
    mr?: number
    mb?: number
    ml?: number
    mx?: number | string
    my?: number
    p?: number
    pt?: number
    pr?: number
    pb?: number
    pl?: number
    px?: number
    py?: number
  }

  export interface BoxProps extends SpaceProps<BoxClass> {
    className?: string
    width?: number | number[]
    fontSize?: number | number[]
    css?: Object
    color?: string
    bg?: string
  }
  type BoxClass = React.StatelessComponent<BoxProps>
  export const Box: BoxClass

  export interface ButtonProps extends BoxProps {
    fontWeight?: string
    border?: number
    borderColor?: string
    borderRadius?: number
    variant?: string
  }
  type ButtonClass = React.StatelessComponent<ButtonProps>
  export const Button: ButtonClass

  export interface CardProps extends BoxProps {
    border?: number
    borderColor?: string
    borderRadius?: number
    boxShadow?: string
    backgroundImage?: string
    backgroundSize?: string
    backgroundPosition?: string
    backgroundRepeat?: string
    opacity?: number
    variant?: string
  }
  type CardClass = React.StatelessComponent<CardProps>
  export const Card: CardClass

  export interface FlexProps extends BoxProps {
    alignItems?: string
    justifyContent?: string
    flexDirection?: string
    flexWrap?: string
  }
  type FlexClass = React.StatelessComponent<FlexProps>
  export const Flex: FlexClass

  export interface ImageProps extends BoxProps {
    height?: number
    borderRadius?: number
    src?: string
    alt?: string
  }
  type ImageClass = React.StatelessComponent<ImageProps>
  export const Image: ImageClass

  export interface LinkProps extends BoxProps {
    href?: string
  }
  type LinkClass = React.StatelessComponent<LinkProps>
  export const Link: LinkClass

  export interface TextProps extends BoxProps {
    fontSize?: number | number[]
    fontWeight?: string
    color?: string
    fontFamily?: string
    textAlign?: string
    lineHeight?: number
    letterSpacing?: number
  }
  type TextClass = React.StatelessComponent<TextProps>
  export const Text: TextClass

  export interface HeadingProps extends TextProps {}
  type HeadingClass = React.StatelessComponent<HeadingProps>
  export const Heading: HeadingClass
}
