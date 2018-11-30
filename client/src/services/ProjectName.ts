import { useState, useEffect } from 'react'
import { Project } from "../model/project"
import { listProjects } from "./projects"

export default function ProjectName(id: string) {
  const [projects, setProjects] = useState<Project[]>([])

  useEffect(() => {
    listProjects().then(res => setProjects(res))
  })

  const project = projects.find(o => o.id === id)

  if (project === undefined) {
    return "Loading..."
  }

  return project.name
}
