import { Project } from "../model/project"

export function listProjects(): Promise<Project[]> {
  const projectsMock = [
    {
      id: "1",
      name: "Continuous Design Deployment",
      description: "Repo de test",
    },
  ]
  return new Promise<Project[]>(resolve => resolve(projectsMock))
}
