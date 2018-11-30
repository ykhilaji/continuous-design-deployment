export function pushPR(fileKey: string, ids: Id[]): Promise<{}> {
  return fetch(`/api/images/${fileKey}`, {
    method: 'POST',
    body: JSON.stringify(ids)
  }).then(response => {
    if (response.ok) {
      return response.json()
    } else {
      throw {
        name: 'FetchError',
        status: response.status
      }
    }
  }).then(json => json.children as {})
}

export interface Id {
  id: string
}