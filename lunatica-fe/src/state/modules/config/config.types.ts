import { State, User, Country, Complaint, ComplaintType } from './../../../types/common.types'

export interface ConfigState {
	user: User,
	cookie: string,
	info: Info,
	complaints: Complaint[]
}

export interface Info {
	complaintStates: State[],
	countries: Country[],
	complaintTypes: ComplaintType[]
}
