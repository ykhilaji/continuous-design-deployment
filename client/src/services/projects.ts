import { Project } from '../model/project'

export function listProjects(): Promise<Project[]> {
  const projectsMock = [
    { id: '1', name: 'Figobool pour AXA', description: 'Encore une application pour une assurance.' },
    { id: '2', name: 'Andy pour BNPP', description: 'Un projet qu\'on a refait 4 fois, tout ca pour finir sur Spring Boot...' }
  ]
  return new Promise<Project[]>((resolve) => resolve(projectsMock))
}
