import { forwardToBackend } from '$lib/server/backendProxy';
import type { RequestHandler } from './$types';

export const POST: RequestHandler = async ({ request }) => {
	const body = await request.json().catch(() => null);

	// Backend expects a JSON body: { userName, password }
	return forwardToBackend({
		method: 'POST',
		backendPath: '/login',
		jsonBody: body
	});
};

