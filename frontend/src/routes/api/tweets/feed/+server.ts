import { forwardToBackend } from '$lib/server/backendProxy';
import type { RequestHandler } from './$types';

export const GET: RequestHandler = async ({ url, request }) => {
	const authHeader = request.headers.get('authorization');
	if (!authHeader) {
		return new Response(JSON.stringify({ message: 'Missing Authorization header' }), {
			status: 401,
			headers: { 'content-type': 'application/json' }
		});
	}

	const page = Number(url.searchParams.get('page') ?? '0');
	const pageSize = Number(url.searchParams.get('pageSize') ?? '10');

	return forwardToBackend({
		method: 'GET',
		backendPath: `/tweets/feed?page=${encodeURIComponent(String(page))}&pageSize=${encodeURIComponent(String(pageSize))}`,
		authHeader
	});
};

