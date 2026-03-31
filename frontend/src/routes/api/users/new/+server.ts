import { forwardToBackend } from '$lib/server/backendProxy';
import type { RequestHandler } from './$types';

export const POST: RequestHandler = async ({ request }) => {
	const body = await request.json().catch(() => null);
	return forwardToBackend({
		method: 'POST',
		backendPath: '/users/new',
		jsonBody: body
	});
};

