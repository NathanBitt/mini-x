import { forwardToBackend } from '$lib/server/backendProxy';
import type { RequestHandler } from './$types';

export const POST: RequestHandler = async ({ request }) => {
	const authHeader = request.headers.get('authorization');
	if (!authHeader) {
		return new Response(JSON.stringify({ message: 'Missing Authorization header' }), {
			status: 401,
			headers: { 'content-type': 'application/json' }
		});
	}

	const body = await request.json().catch(() => null);
	return forwardToBackend({
		method: 'POST',
		backendPath: '/tweets/new',
		authHeader,
		jsonBody: body
	});
};

